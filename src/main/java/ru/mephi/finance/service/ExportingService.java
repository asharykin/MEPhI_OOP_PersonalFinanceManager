package ru.mephi.finance.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;

public interface ExportingService {

    void dumpData();

    void writeDataToFile(File file);

    void exportData(ObjectNode node);
}
