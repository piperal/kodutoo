package ee.piperal.veebipood.dto;


//Api endpoint for one model

public record PersonLoginRecordDto(
        String email,
        String password
) {
}
