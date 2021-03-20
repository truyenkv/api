package company.core.api;

import lombok.Data;

@Data
public class InitConnection {
    private String dataBaseName;
    private String schemaName;

    public InitConnection(String dataBaseName, String schemaName){
        this.dataBaseName = dataBaseName;
        this.schemaName = schemaName;
    }
}
