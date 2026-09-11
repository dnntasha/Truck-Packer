package rw344.truckpacker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class InputLoader {
    //adding two mappers with JSON and YAML
    // YAML uses the same @JsonProperties that JSOM already relies on
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper yamlMapper = new YAMLMapper();

    public TruckConfig load(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Input file not found: " + path);
        }
        ObjectMapper mapper = selectMapper(path);
        return mapper.readValue(file, TruckConfig.class);
    }

    private ObjectMapper selectMapper(String path) throws IOException {
        String lower = path.toLowerCase();
        if (lower.endsWith(".json")) {
            return jsonMapper;
        } else if (lower.endsWith(".yaml") || lower.endsWith(".yml")) {
            return ymalMapper;
        } else {
            throw new IOException("Unsupported input file extension for '" + path + "'. Supported formats are: .json .yaml/.yml");
        }
    }
}
