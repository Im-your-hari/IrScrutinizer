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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.harctoolbox.girr.Command;
import org.harctoolbox.girr.GirrException;
import org.harctoolbox.girr.Remote;
import org.harctoolbox.girr.RemoteSet;
import org.harctoolbox.ircore.IrCoreException;
import org.harctoolbox.irp.IrpException;

/**
 *
 */
public interface IRemoteSetExporter extends ICommandExporter {
    public void export(RemoteSet remoteSet, String title, int count, boolean automaticFilenames,
            Component parent, File exportDir, String charsetName)
            throws IOException, TransformerException, IrCoreException, IrpException, GirrException;

    public abstract void export(RemoteSet remoteSet, String title, int count, File saveFile, String charsetName)
            throws IOException, TransformerException, GirrException, IrpException, IrCoreException;

    public void export(Remote remote, String title, String source, int count, File saveFile, String charsetName)
            throws IOException, TransformerException, IrCoreException, IrpException, GirrException;

    public void export(Map<String, Command> commands, String source, String title,
            Remote.MetaData metaData,
            int count, File saveFile, String charsetName) throws IOException, TransformerException, GirrException, IrCoreException, IrpException;

    public File export(Map<String, Command> commands, String source, String title,
            Remote.MetaData metaData, int count, boolean automaticFilenames, Component parent, File exportDir, String charsetName)
            throws IOException, TransformerException, IrCoreException, IrpException, GirrException;

    public void export(Collection<Command> commands, String source, String title, int count, File saveFile, String charsetName)
            throws IOException, TransformerException, IrCoreException, IrpException, GirrException;

    public boolean supportsEmbeddedFormats();
}
