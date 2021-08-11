/*
Copyright (C) 2013, 2014, 2021 Bengt Martensson.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program. If not, see http://www.gnu.org/licenses/.
*/

package org.harctoolbox.guicomponents;

import java.awt.Cursor;
import java.io.IOException;
import org.harctoolbox.harchardware.HarcHardwareException;
import org.harctoolbox.harchardware.comm.NonExistingPortException;
import static org.harctoolbox.harchardware.ir.IIrReader.DEFAULT_BEGIN_TIMEOUT;
import org.harctoolbox.harchardware.ir.IrWidget;
import org.harctoolbox.ircore.ModulatedIrSequence;

/**
 *
 */
public final class IrWidgetBean extends SerialHardwareBean {

    public IrWidgetBean() {
        this(null);
    }

    public IrWidgetBean(GuiUtils guiUtils) {
        this(guiUtils, false, DEFAULT_BEGIN_TIMEOUT, null);
    }

    public IrWidgetBean(GuiUtils guiUtils, boolean verbose, int timeout, String initialPort) {
        super(guiUtils, verbose, timeout);
        initComponents();
        setupPortComboBox(portComboBox, initialPort);
    }

    @Override
    protected void setupHardware() throws IOException, NonExistingPortException {
        hardware = new IrWidget(portName, verbose, timeout);
    }

    @Override
    protected void setVersion() {
    }

    private void setupPortComboBox(boolean useCached) throws IOException {
        if (hardware != null)
            hardware.close();

        portComboBox.setModel(createModel(useCached));
    }

    @Override
    protected void enableStuff(boolean isOpen) {
        portComboBox.setEnabled(!isOpen);
    }

    @Override
    public boolean canCapture() {
        return true;
    }

    @Override
    public ModulatedIrSequence capture() throws IOException {
        return ((IrWidget) hardware).capture();
    }

    @Override
    public String getName() {
        return IrWidget.IRWIDGET;
    }

    @Override
    protected void enableOpenToggleButton(boolean enabled) {
        openToggleButton.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        portComboBox = new javax.swing.JComboBox<>();
        refreshButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        openToggleButton = new javax.swing.JToggleButton();

        setPreferredSize(new java.awt.Dimension(800, 80));

        portComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { NOT_INITIALIZED }));
        portComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portComboBoxActionPerformed(evt);
            }
        });

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Crystal-Clear/22x22/actions/reload.png"))); // NOI18N
        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("Reload list of available serial ports.");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Serial Port");

        openToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Crystal-Clear/22x22/actions/connect_creating.png"))); // NOI18N
        openToggleButton.setText("Open");
        openToggleButton.setToolTipText("Open or close connection to device.");
        openToggleButton.setEnabled(false);
        openToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(refreshButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(portComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshButton)
                    .addComponent(openToggleButton))
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        Cursor oldCursor = setBusyCursor();
        try {
            setupPortComboBox(false);
        } catch (IOException ex) {
            guiUtils.error(ex);
        } finally {
            resetCursor(oldCursor);
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void portComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portComboBoxActionPerformed
        setPortName((String) portComboBox.getSelectedItem());
    }//GEN-LAST:event_portComboBoxActionPerformed

    private void openToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openToggleButtonActionPerformed
        Cursor oldCursor = setBusyCursor();
        boolean opening = openToggleButton.isSelected();
        try {
            openClose(opening);
        } catch (HarcHardwareException | IOException ex) {
            guiUtils.error(ex);
        } finally {
            openToggleButton.setSelected(isOpen());
            //refreshButton.setEnabled(!hardware.isValid());
            resetCursor(oldCursor);
        }
    }//GEN-LAST:event_openToggleButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton openToggleButton;
    private javax.swing.JComboBox<String> portComboBox;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables
}
