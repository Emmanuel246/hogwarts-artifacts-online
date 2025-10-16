package com.hogwart.hogwarts_artifacts_online.artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String id) {

        super("Could not find Artifact with id: " + id + " :(");
    }

}
