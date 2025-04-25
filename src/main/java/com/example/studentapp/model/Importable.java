package com.example.studentapp.model;

import java.io.File;
import java.util.List;

public interface Importable<T> {
    List<T> importFromFile(File file);
}
