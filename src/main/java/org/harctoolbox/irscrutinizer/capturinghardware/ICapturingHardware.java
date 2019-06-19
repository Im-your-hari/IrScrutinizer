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

package org.harctoolbox.irscrutinizer.capturinghardware;

import java.io.IOException;
import javax.swing.JPanel;
import org.harctoolbox.harchardware.HarcHardwareException;
import org.harctoolbox.harchardware.IHarcHardware;
import org.harctoolbox.harchardware.ir.ICapture;
import org.harctoolbox.ircore.InvalidArgumentException;
import org.harctoolbox.ircore.ModulatedIrSequence;

/**
 *
 * @param <T>
 */
public interface ICapturingHardware <T extends ICapture & IHarcHardware> {

    public String getName();

    public T getCapturer();

    public JPanel getPanel();

    public void setVerbose(boolean verbose);

    public void close();

    public ModulatedIrSequence capture() throws HarcHardwareException, IOException, InvalidArgumentException;

    public boolean stopCapture();

    /**
     * This function (re-)initializes the hardware. The construction should not.
     * If the parameters has not changed, should not re-initialize.
     * @throws IOException
     * @throws HarcHardwareException
     */
    public void setup() throws IOException, HarcHardwareException;

    public boolean isValid();

    public String getVersion() throws IOException;
}
