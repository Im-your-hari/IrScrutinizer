/*
Copyright (C) 2013 Bengt Martensson.

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

import javax.swing.JButton;

public class HelpButton extends JButton {
    private GuiUtils guiUtils;
    private String helpText;
    public HelpButton(GuiUtils guiUtils, String helpText) {
        this();
        this.guiUtils = guiUtils;
        this.helpText = helpText;
    }

    public HelpButton() {
        super();

        super.setIcon(new javax.swing.ImageIcon(HelpButton.class.getResource("/icons/Crystal-Clear/22x22/actions/help.png"))); // NOI18N
        super.setMnemonic('H');
        super.setText("Help");
        super.setToolTipText("Display help.");
        super.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        super.addActionListener((java.awt.event.ActionEvent evt) -> {
            guiUtils.help(helpText);
        });
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public void setGuiUtils(GuiUtils guiUtils) {
        this.guiUtils = guiUtils;
    }
}
