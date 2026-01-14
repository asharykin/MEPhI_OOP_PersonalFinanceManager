package ru.mephi.finance.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;

public interface ImportingService {

    void loadData();

    JsonNode readDataFromFile(File file);

    void importData(JsonNode node);
}
