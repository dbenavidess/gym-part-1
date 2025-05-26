package com.dbenavidess.gym_part_1.infrastructure.controller;

import com.dbenavidess.gym_part_1.domain.model.TrainingType;
import com.dbenavidess.gym_part_1.domain.repository.TrainingTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Training types", description = "Training types operations")
@RestController
public class TrainingTypeController {
    private final TrainingTypeRepository repository;

    public TrainingTypeController(TrainingTypeRepository repository) {
        this.repository = repository;
    }
    @Operation(summary = "Get all training types")
    @ApiResponse(responseCode = "200", description = "Get all training types",
            content = { @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TrainingType.class))) })
    @GetMapping("/training-types")
    public List<TrainingType> getAllTrainingTypes(){
        return repository.getAll();
    }
}
