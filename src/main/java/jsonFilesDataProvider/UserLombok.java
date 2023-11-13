package jsonFilesDataProvider;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class UserLombok {

    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;
    private String message;


}


