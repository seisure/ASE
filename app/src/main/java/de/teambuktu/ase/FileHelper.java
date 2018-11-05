package de.teambuktu.ase;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileHelper {
    static File exportToCSV(ArrayList<Condition> conditionList, ArrayList<Action> actionList) {
        StringBuilder builder = new StringBuilder();

        for (Condition condition : conditionList) {
            builder.append('C').append(';');
            String title = condition.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (String rule :
                    condition.rules) {
                builder.append(rule).append(';');
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append('\n');
        }

        for (Action action : actionList) {
            builder.append('A').append(';');
            String title = action.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (Boolean rule :
                    action.rules) {
                builder.append(rule ? 1 : 0).append(';');
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append('\n');
        }

        try {
            File file;
            FileOutputStream outputStream;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ASE.csv");
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            outputStream.write(builder.toString().getBytes());
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return new File("");
        }
    }

    static ArrayList<TableEntry> loadFromCSV(InputStream file) {
        ArrayList<String> csv = readFromFile(file);
        int rules = csv.get(0).split(";").length - 2;
        ArrayList<TableEntry> entries = new ArrayList<>();

        for (String line : csv) {
            String[] lineParts = line.split(";");
            if (lineParts[0].equals("C")) {
                Condition newCondition = new Condition(rules);
                newCondition.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    newCondition.rules.set(j++, lineParts[i]);
                }
                entries.add(newCondition);
            }
            else if (lineParts[0].equals("A")) {
                Action newAction = new Action(rules);
                newAction.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    newAction.rules.set(j++, lineParts[i].equals("1"));
                }
                entries.add(newAction);
            }
        }

        return entries;
    }

    private static ArrayList<String> readFromFile(InputStream file) {
        ArrayList<String> ret = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ret.add(line);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
