package io.example.authorization.domain.common.resource;

import io.example.authorization.controller.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ErrorsEntityModel extends EntityModel<Errors> {

    public ErrorsEntityModel(Errors content, Link... links) {
        super(content, links);
        add(linkTo(IndexController.class).withRel(INDEX));
    }
}