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

package org.harctoolbox.irscrutinizer.capturinghardware;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import org.harctoolbox.guicomponents.GuiUtils;
import org.harctoolbox.guicomponents.SerialPortSimpleBean;
import org.harctoolbox.harchardware.HarcHardwareException;
import org.harctoolbox.harchardware.IHarcHardware;
import org.harctoolbox.harchardware.comm.NonExistingPortException;
import org.harctoolbox.harchardware.ir.ICapture;
import org.harctoolbox.harchardware.ir.IrSerial;
import org.harctoolbox.ircore.ThisCannotHappenException;
import org.harctoolbox.irscrutinizer.Props;

/**
 * This class does something interesting and useful. Or not...
 * @param <T>
 */

// This code sucks, but less than before.
public class CapturingSerial <T extends ICapture & IHarcHardware> extends CapturingHardware<T> implements ICapturingHardware<T> {

    private SerialPortSimpleBean serialPortSimpleBean;
    private String portName = null;
    private int baudRate = -1;
    private Class<T> clazz;
    private T hardware;

    public CapturingSerial(final Class<T> clazz, JPanel panel, SerialPortSimpleBean serialPortSimpleBean,
            Props properties_, GuiUtils guiUtils_,
            CapturingHardwareManager capturingHardwareManager) {
        super(panel, properties_, guiUtils_, capturingHardwareManager);
        // Thing are be initialized through the serialPortSerialBean.
        this.serialPortSimpleBean = serialPortSimpleBean;
        this.baudRate = serialPortSimpleBean.getBaudRate();
        this.portName = serialPortSimpleBean.getPortName();
        this.clazz = clazz;
        hardware = null;

        serialPortSimpleBean.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            String propertyName = evt.getPropertyName();

            try {
                switch (propertyName) {
                    case SerialPortSimpleBean.PROP_BAUD:
                        setup();
                        break;
                    case SerialPortSimpleBean.PROP_PORTNAME:
                        if (evt.getNewValue() == null)
                            return;
                        setup();
                        break;
                        //case SerialPortSimpleBean.PROP_VERSION:
                    case SerialPortSimpleBean.PROP_ISOPEN:
                        break;
                    default:
                        throw new ThisCannotHappenException("Programmming error detected");
                }
            } catch (IOException ex) {
                guiUtils.error(ex);
            }
        });
    }

    @Override
    public void setup() throws IOException {
        String newPort = serialPortSimpleBean.getPortName();
        int newBaud = serialPortSimpleBean.getBaudRate();
        if (hardware != null && (newPort == null || newPort.equals(portName)) && (baudRate == newBaud))
            return;

        if (hardware != null)
            hardware.close();
        hardware = null;
        try {
            portName = newPort;
            if (IrSerial.class.isAssignableFrom(clazz)) {
                hardware = clazz.getConstructor(String.class, boolean.class, Integer.class, Integer.class, Integer.class, Integer.class).newInstance(
                        newPort, properties.getVerbose(), newBaud,
                        properties.getCaptureBeginTimeout(), properties.getCaptureMaxSize(), properties.getCaptureEndingTimeout());
                baudRate = newBaud;
                Props.class.getMethod("set" + clazz.getSimpleName() + "CapturePortBaudRate", int.class).invoke(properties, newBaud);
            } else {
                hardware = clazz.getConstructor(String.class, boolean.class, Integer.class, Integer.class, Integer.class).newInstance(
                        newPort, properties.getVerbose(),
                        properties.getCaptureBeginTimeout(), properties.getCaptureMaxSize(), properties.getCaptureEndingTimeout());
            }
            Props.class.getMethod("set" + clazz.getSimpleName() + "CapturePortName", String.class).invoke(properties, portName);
            serialPortSimpleBean.setHardware(hardware);
            selectMe();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | HarcHardwareException ex) {
            guiUtils.error(ex);
        } catch (InvocationTargetException ex) {
            // Likely NoSuchPortException
            if (NonExistingPortException.class.isInstance(ex.getCause()))
                throw new IOException("No such port: " + newPort);
            else
                guiUtils.error(ex);
            guiUtils.error(ex.getCause().getMessage());
        }
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public void open() throws HarcHardwareException, IOException {
        hardware.open();
    }

    @Override
    public void setDebug(int debug) {
    }

    @Override
    public T getCapturer() {
        return hardware;
    }
}
