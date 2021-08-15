/*
Copyright (C) 2013, 2014 Bengt Martensson.

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

package org.harctoolbox.irscrutinizer.exporter;

import com.neuron.app.tonto.ActionIRCode;
import com.neuron.app.tonto.CCF;
import com.neuron.app.tonto.CCFButton;
import com.neuron.app.tonto.CCFColor;
import com.neuron.app.tonto.CCFDevice;
import com.neuron.app.tonto.CCFFont;
import com.neuron.app.tonto.CCFIRCode;
import com.neuron.app.tonto.CCFIconSet;
import com.neuron.app.tonto.CCFNode;
import com.neuron.app.tonto.CCFPanel;
import com.neuron.app.tonto.ProntoModel;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.harctoolbox.girr.Command;
import org.harctoolbox.girr.GirrException;
import org.harctoolbox.girr.Remote;
import org.harctoolbox.girr.RemoteSet;
import org.harctoolbox.ircore.IrCoreException;
import org.harctoolbox.irp.IrpException;
import org.harctoolbox.xml.XmlUtils;
import org.w3c.dom.DocumentFragment;

/**
 * This class does something interesting and useful. Or not...
 */
public class ProntoClassicExporter extends RemoteSetExporter {

    private static final int buttonLabelLength = 100;
    public static final String[] prontoModelNames;
    private static final DocumentFragment documentation = XmlUtils.stringToDocumentFragment("ProntoClassicExporter documentation not written yet.");

    static {
        ProntoModel[] prontomodels = ProntoModel.getModels();
        prontoModelNames = new String[prontomodels.length];
        for (int i = 0; i < prontomodels.length; i++)
            prontoModelNames[i] = prontomodels[i].toString();
    }

    private final ProntoModel prontoModel;
    private CCF ccf;
    private final int buttonWidth;
    private final int buttonHeight;
    private int screenWidth;
    private int screenHeight;

    public ProntoClassicExporter(ProntoModel prontomodel,
            int buttonwidth, int buttonheight, int screenwidth, int screenheight) {
        super();
        this.ccf = null;
        this.prontoModel = prontomodel;
        this.buttonWidth = buttonwidth;
        this.buttonHeight = buttonheight;
        this.screenWidth = screenwidth;
        this.screenHeight = screenheight;
    }

    @Override
    public String[][] getFileExtensions() {
        return new String[][] { new String[]{ "ccf", "Pronto classic configuration files (*.ccf)" } };
    }

    @Override
    public String getPreferredFileExtension() {
        return "ccf";
    }

    @Override
    public String getFormatName() {
        return "ProntoClassic";
    }

    @Override
    public DocumentFragment getDocumentation() {
        return documentation;
    }

    @Override
    protected boolean isExecutable() {
        return false;
    }

    @Override
    public void export(RemoteSet remoteSet, String title, int count, File saveFile, String charsetName /* ignored */)
            throws IOException, GirrException, IrpException, IrCoreException {
        setup(remoteSet);
        ccf.save(saveFile.getPath());
    }

    private void setup(RemoteSet remoteSet) throws GirrException, IrpException, IrCoreException {
        if (prontoModel.getModel() != ProntoModel.CUSTOM) {
            screenWidth = prontoModel.getScreenSize().width;
            screenHeight = prontoModel.getScreenSize().height;
        }
        int usedScreenHeight = screenHeight;
        int usedScreenWidth = screenWidth;

        int rows = usedScreenHeight / buttonHeight;
        int columns = usedScreenWidth / buttonWidth;
        int vRest = usedScreenHeight % buttonHeight;
        int vRestPart = rows > 1 ? vRest / (rows - 1) : 0;
        int hRest = usedScreenWidth % buttonWidth;
        int hRestPart = columns > 1 ? hRest / (columns - 1) : 0;
        ccf = new CCF(prontoModel);
        if (prontoModel.getModel() == ProntoModel.CUSTOM)
            ccf.setScreenSize(screenWidth, screenHeight);
        ccf.setVersionString("IrScrutinizer CCF");
        for (Remote remote : remoteSet.getRemotes()) {
            String remoteName = remote.getName();
            @SuppressWarnings("deprecation")
            Collection<Command> cmds = remote.getCommands();
            Iterator<Command> it = cmds.iterator();
            //it.hasNext();)
            CCFDevice dev = ccf.createDevice(remoteName);
            ccf.appendDevice(dev);
            for (int panelNo = 0; panelNo < (int) (((double) cmds.size()) / (rows * columns) + 0.999); panelNo++) {
                CCFPanel panel = dev.createPanel(remoteName + "_" + "codes_" + (panelNo + 1));
                dev.addPanel(panel);

                for (int x = 0; x < columns; x++) {
                    for (int y = 0; y < rows; y++) {
                        if (!it.hasNext())
                            break;

                        Command cmd = it.next();
                        String buttonName = cmd.getName();
                        if (buttonName.length() > buttonLabelLength)
                            buttonName = buttonName.substring(0, buttonLabelLength);
                        CCFButton b1 = panel.createButton(buttonName);
                        b1.setFont(CCFFont.SIZE_8);
                        b1.setTextAlignment(CCFNode.TEXT_RIGHT);
                        int xPos = x * (buttonWidth + hRestPart);
                        int yPos = y * (buttonHeight + vRestPart);
                        b1.setLocation(new Point(xPos, yPos));
                        b1.setSize(new Dimension(buttonWidth, buttonHeight));
                        panel.addButton(b1);
                        String ccfstring = cmd.getProntoHex();

                        if ((prontoModel.getModel() == ProntoModel.CUSTOM) || (prontoModel.getCapability() & (1 << 18)) != 0)
                            ccfstring = "0000 0000 0000 " + ccfstring;
                        CCFIRCode code = new CCFIRCode(dev.getHeader(), ccfstring);
                        b1.appendAction(new ActionIRCode(code));

                        CCFIconSet iconSet = b1.getIconSet();
                        iconSet.setForeground(CCFIconSet.ACTIVE_UNSELECTED, CCFColor.getColor(CCFColor.BLACK));
                        iconSet.setBackground(CCFIconSet.ACTIVE_UNSELECTED, CCFColor.getColor(245/*CCFColor.LIGHT_GRAY*/));
                        iconSet.setForeground(CCFIconSet.ACTIVE_SELECTED, CCFColor.getColor(245/*CCFColor.LIGHT_GRAY*/));
                        iconSet.setBackground(CCFIconSet.ACTIVE_SELECTED, CCFColor.getColor(CCFColor.BLACK));
                        b1.setIconSet(iconSet);
                    }
                }
            }
        }
    }
}
