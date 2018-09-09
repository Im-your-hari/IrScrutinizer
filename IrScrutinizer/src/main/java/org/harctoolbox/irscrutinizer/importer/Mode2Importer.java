/*
Copyright (C) 2014 Bengt Martensson.

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

package org.harctoolbox.irscrutinizer.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import org.harctoolbox.ircore.IrCoreUtils;
import org.harctoolbox.ircore.ModulatedIrSequence;

/**
 * This class imports Lirc's mode2 files.
 */
public class Mode2Importer extends ReaderImporter implements IModulatedIrSequenceImporter,IReaderImporter {

    private ModulatedIrSequence sequence;

    @Override
    public void load(Reader reader, String origin) throws IOException, ParseException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<Integer> data = new ArrayList<>(256);
        boolean lastWasPulse = false;
        int lineNo = 0;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null)
                break;
            lineNo++;
            if (line.isEmpty())
                continue;
            int duration;
            boolean isPulse;
            if (line.startsWith("pulse")) {
                isPulse = true;
            } else if (line.startsWith("space")) {
                isPulse = false;
                if (data.isEmpty()) // Ignore leading space
                    continue;
            } else
                throw new ParseException("Unknown line: " + line, lineNo);

            try {
                duration = Integer.parseInt(line.substring(6));
            } catch (NumberFormatException e) {
                throw new ParseException("Unparsable data: " + line, lineNo);
            }
            if (lastWasPulse != isPulse)
                data.add(duration);
            else {
                int index = data.size() - 1;
                data.set(index, data.get(index) + duration);
            }
            lastWasPulse = isPulse;
        }
        if (data.size() % 2 != 0)
            data.add((int)(IrCoreUtils.milliseconds2microseconds(getEndingTimeout())));
        int[] array = new int[data.size()];
        int i = 0;
        for (Integer duration : data) {
            array[i] = duration;
            i++;
        }
        //try {
        //    sequence = new ModulatedIrSequence(array, getFallbackFrequency());
        //} catch (OddSequenceLengthException ex) {
        //    throw new ParseException(ex.getMessage(), lineNo);
            // TODO: invoke logger
        //}
    }

    @Override
    public String[][] getFileExtensions() {
        return new String[][]{new String[]{"Mode2 files (*.mode2 *.txt)", "mode2", "txt" }};
   }

    @Override
    public String getFormatName() {
        return "Mode2";
    }

    @Override
    public ModulatedIrSequence getModulatedIrSequence() {
        return sequence;
    }
}
