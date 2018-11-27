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
import java.util.List;

class FileHelper {
    static File exportToCsv(List<Condition> conditions, List<Action> actions, String filename) {
        StringBuilder builder = new StringBuilder();

        for (Condition condition : conditions) {
            builder.append('C').append(';');
            String title = condition.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (Rule rule : condition.rules) {
                builder.append(rule.getRuleConditionValue()).append(';');
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append('\n');
        }

        for (Action action : actions) {
            builder.append('A').append(';');
            String title = action.getTitle();
            builder.append(title == null ? "" : title).append(';');
            for (Rule rule : action.rules) {
                builder.append(rule.getRuleActionValue() ? 1 : 0).append(';');
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append('\n');
        }

        return buildFile(builder.toString(), filename);
    }

    /**
     * Create a file with content.
     *
     * @param fileContent Serialized content to write
     * @param fileName    the target-filename
     */

    static File buildFile(String fileContent, String fileName) {

        try {
            File file;
            FileOutputStream outputStream;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return new File("");
        }
    }

    static ArrayList<TableEntry> loadFromCsv(InputStream file) {
        ArrayList<String> csv = readFromFile(file);
        int rules = csv.get(0).split(";").length - 2;
        if (rules < 0) {
            return new ArrayList<>();
        }
        ArrayList<TableEntry> entries = new ArrayList<>();

        for (String line : csv) {
            String[] lineParts = line.split(";");
            if (lineParts.length != rules + 2 || !lineParts[0].equals("A") && !lineParts[0].equals("C")) {
                return new ArrayList<>();
            }
            if (lineParts[0].equals("C")) {
                Condition newCondition = new Condition(rules);
                newCondition.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    if (lineParts[i].equals("J") || lineParts[i].equals("N") || lineParts[i].equals("-")) {
                        newCondition.rules.get(j++).setRuleConditionValue(lineParts[i]);
                    } else {
                        return new ArrayList<>();
                    }
                }
                entries.add(newCondition);
            } else if (lineParts[0].equals("A")) {
                Action newAction = new Action(rules);
                newAction.setTitle(lineParts[1]);
                int j = 0;
                for (int i = 2; i < lineParts.length; i++) {
                    if (lineParts[i].equals("1") || lineParts[i].equals("0")) {
                        newAction.rules.get(j++).setRuleActionValue(lineParts[i].equals("1"));
                    } else {
                        return new ArrayList<>();
                    }
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
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
