package com.moneydance.modules.features.importlist.view;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * @author <a href="mailto:&#102;&#108;&#111;&#114;&#105;&#97;&#110;&#46;&#106;
 *&#46;&#98;&#114;&#101;&#117;&#110;&#105;&#103;&#64;&#109;&#121;&#45;&#102;
 *&#108;&#111;&#119;&#46;&#99;&#111;&#109;">Florian J. Breunig</a>
 */
class HeaderRenderer implements TableCellRenderer {

    private static final long serialVersionUID = 3121884943197710031L;
    private final TableCellRenderer defaultHeaderTableCellRenderer;

    HeaderRenderer(
            final TableCellRenderer argDefaultHeaderTableCellRenderer) {
        this.defaultHeaderTableCellRenderer = argDefaultHeaderTableCellRenderer;
    }

    public final Component getTableCellRendererComponent(
            final JTable table,
            final Object value,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int column) {
        Component component =
            this.defaultHeaderTableCellRenderer.getTableCellRendererComponent(
            table, value, hasFocus, hasFocus, row, column);
        ColorSchemeHelper.applyColorScheme(component, row);

        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            jComponent.setBorder(null);
        }

        if (component instanceof JLabel) {
            JLabel jLabel = (JLabel) component;
            jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return component;
    }
}
