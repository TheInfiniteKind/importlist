/*
 * Import List - http://my-flow.github.com/importlist/
 * Copyright (C) 2011 Florian J. Breunig
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.moneydance.modules.features.importlist.io;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.view.HomePageView;
import com.moneydance.modules.features.importlist.util.Preferences;

/**
 * This core class coordinates and delegates operations on the file system.
 */
public class FileAdmin {

    /**
     * Static initialization of class-dependent logger.
     */
    private static Logger log = Logger.getLogger(FileAdmin.class);

    private final Preferences               prefs;
    private FeatureModuleContext            context;
    private final DirectoryChooser          directoryChooser;
    private final AbstractFileFilter        transactionFileFilter;
    private final AbstractFileFilter        textFileFilter;
    private final AbstractFileFilter        readableFileFilter;
    private FileAlterationObserver          observer;
    private final TransactionFileListener   listener;
    private final FileAlterationMonitor     monitor;
    private final List<File>                files;
    private boolean                         dirty;
    private HomePageView                    homePageView;

    public FileAdmin(final String baseDirectory) {
        this.prefs            = Preferences.getInstance();
        this.directoryChooser = new DirectoryChooser(baseDirectory);
        this.transactionFileFilter = new SuffixFileFilter(
                this.prefs.getTransactionFileExtensions(),
                IOCase.INSENSITIVE);
        this.textFileFilter = new SuffixFileFilter(
                this.prefs.getTextFileExtensions(),
                IOCase.INSENSITIVE);
        this.readableFileFilter = new AndFileFilter(
                CanReadFileFilter.CAN_READ,
                new OrFileFilter(
                        this.transactionFileFilter,
                        this.textFileFilter)
        );

        this.listener = new TransactionFileListener(this);
        this.monitor  = new FileAlterationMonitor(
                this.prefs.getMonitorIntervall());
        this.setFileMonitorToCurrentImportDir();

        this.files = new ArrayList<File>();

        this.dirty = true;
    }

    public final void setContext(final FeatureModuleContext argContext) {
        this.context = argContext;
    }

    public final void reset() {
        log.info("Resetting base directory.");
        this.directoryChooser.reset();
        this.monitor.removeObserver(this.observer);
        this.setFileMonitorToCurrentImportDir();
        this.setDirty(true);
    }

    public final List<File> getFiles() {
        return this.files;
    }

    public final void reloadFiles() {
        this.files.clear();
        try {
            Collection<File> collection = FileUtils.listFiles(
                    this.getBaseDirectory(),
                    this.readableFileFilter,
                    null); // ignore subdirectories
            this.files.addAll(collection);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public final File getBaseDirectory() {
        return this.directoryChooser.getBaseDirectory();
    }

    public final void setDirty(final boolean argDirty) {
        this.dirty = argDirty;
        if (this.dirty && this.homePageView != null) {
            log.info("Base directory contains changes.");
            this.reloadFiles();
            this.homePageView.refresh();
        }
    }

    public final boolean isDirty() {
        return this.dirty;
    }

    /**
     * Start monitoring the current import directory.
     */
    public final void startMonitor() {
        log.debug("Starting the directory monitor.");
        try {
            this.monitor.start();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * Stop monitoring the current import directory.
     */
    public final void stopMonitor() {
        log.debug("Stopping the directory monitor.");
        try {
            this.monitor.stop();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    public final void setHomePageView(final HomePageView argHomePageView) {
        Validate.notNull(argHomePageView, "argHomePageView can't be null");
        this.homePageView = argHomePageView;
        this.homePageView.refresh();
        this.startMonitor();
    }

    public final void importRow(final int rowNumber) {
        File file = this.files.get(rowNumber);
        log.info("Importing file " + file.getAbsoluteFile());

        String callUri = this.prefs.getTransactionFileImportUriPrefix()
        + file.getAbsolutePath();

        if (this.textFileFilter.accept(file)) {
            callUri = this.prefs.getTextFileImportUriPrefix()
            + file.getAbsolutePath();
        }

        // Import the file identified by the file parameter
        this.context.showURL(callUri);
    }

    public final void deleteRow(final int rowNumber) {
        File file = this.files.get(rowNumber);
        log.info("Deleting file " + file.getAbsoluteFile());

        final String confirmationMessage =
            this.prefs.getConfirmationMessageDeleteFile(file.getName());
        Object confirmationLabel = new JLabel(confirmationMessage);
        Icon icon   = null;
        Image image = this.prefs.getIconImage();
        if (image != null) {
            icon = new ImageIcon(image);
        }
        Object[] options = {
                this.prefs.getOptionDeleteFile(),
                this.prefs.getOptionCancel()
        };

        int choice = JOptionPane.showOptionDialog(
                null, // no parent component
                confirmationLabel,
                null, // no title
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                icon,
                options,
                options[1]);

        if (choice == 0) {
            log.info("Deleting file " + file.getAbsoluteFile());

            if (file.delete()) {
                log.info("Deleted file " + file.getAbsoluteFile());
            } else {
                log.warn("Could not delete file "
                        + file.getAbsoluteFile());
                final String errorMessage =
                    this.prefs.getErrorMessageDeleteFile(file.getName());
                Object errorLabel = new JLabel(errorMessage);
                JOptionPane.showMessageDialog(
                        null, // no parent component
                        errorLabel,
                        null, // no title
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            log.info("Canceled deleting file " + file.getAbsoluteFile());
        }

        this.setDirty(true);
    };

    private void setFileMonitorToCurrentImportDir() {
        this.observer  = new FileAlterationObserver(
                this.getBaseDirectory(),
                this.readableFileFilter,
                IOCase.SENSITIVE);
        this.observer.addListener(this.listener);
        this.monitor.addObserver(this.observer);
    }
}
