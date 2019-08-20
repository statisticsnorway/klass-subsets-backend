package no.ssb.klass.subsets.consumer.klass.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassificationVersionData implements SourceData {

    private List<ClassificationItemData> classificationItems = new ArrayList<>();

    @Override
    public String findNameForCode(String code) {
        for (ClassificationItemData item : classificationItems) {
            if (code.equals(item.getCode())) {
                return item.getName();
            }
        }
        throw new RuntimeException("Code Not Found");
    }
}
