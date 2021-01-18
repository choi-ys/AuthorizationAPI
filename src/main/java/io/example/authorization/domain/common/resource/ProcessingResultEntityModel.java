package io.example.authorization.domain.common.resource;

import io.example.authorization.controller.IndexController;
import io.example.authorization.domain.common.ProcessingResult;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProcessingResultEntityModel extends EntityModel<ProcessingResult> {

    public ProcessingResultEntityModel(ProcessingResult processingResult, Link... links) {
        super(processingResult, links);
        add(linkTo(IndexController.class).withRel(INDEX));
    }
}