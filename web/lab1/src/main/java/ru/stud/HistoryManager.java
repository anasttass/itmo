package ru.stud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryManager {
    private final List<HitRecord> records = new ArrayList<>();

    public void add(HitRecord record) {
        records.add(record);
    }

    public List<HitRecord> getAll() {
        return Collections.unmodifiableList(records);
    }
}

