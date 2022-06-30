package ru.gachigame.game.screen.parts;

import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableOfRecords {
    private static final Map<String, Float> nicknameAndRecord = new HashMap<>();
    private static final Array<Float> times = new Array<>();
    private static final Array<String> nicknames = new Array<>();

    public static StringBuilder sortTable(StringBuilder rawTable) {
        int countRecord = 1;
        String[] tableArray = rawTable.toString().split("[ \n]");

        int length = tableArray.length;
        String nickname;
        float time;
        for (int id = 1; id < length; id += 3) {
            nickname = tableArray[id];
            time = Float.parseFloat(tableArray[id + 1]);
            if (nicknameAndRecord.containsKey(nickname)) {
                if (nicknameAndRecord.get(nickname) > time) {
                    nicknameAndRecord.put(nickname, time);
                    times.add(time);
                }
            } else {
                nicknameAndRecord.put(nickname, time);
                times.add(time);
                nicknames.add(nickname);
            }
        }

        times.sort();

        StringBuilder table = new StringBuilder();

        for (int identifier = 0; identifier < times.size; identifier++) {
            time = times.get(identifier);
            for (int id = 0; id < nicknameAndRecord.size(); id++) {
                nickname = nicknames.get(id);
                if (time == nicknameAndRecord.get(nickname)) {
                    table.append(identifier + 1).append(" ").append(nickname).append(" ").append(time).append("\n");
                    countRecord++;
                }
            }
        }


        if (countRecord > 30) {
            StringBuilder shortTable = new StringBuilder();
            tableArray = table.toString().split("\n");
            for (int identifier = 0; identifier < 30; identifier++) {
                shortTable.append(tableArray[identifier]).append("\n");
            }
            table = shortTable;
        }


        return table;
    }

    public static void addRecord(float time, String nickname){
        //Файл считывается, таблица приводится к правильному формату и записывается в переменную
        StringBuilder table = ReadingAndWritingTheDatabase.readingFile();
        table.replace(table.length()-3, table.length()-1, "");
        table.append(31).append(" ").append(nickname).append(" ").append(time).append("\n");
        table = sortTable(table);
        try {
            ReadingAndWritingTheDatabase.writingFile(table.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
