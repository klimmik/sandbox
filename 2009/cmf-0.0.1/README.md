# CMF v.0.0.1

Old abandoned sandbox project, uploaded just for history :-)

## Intro

The main idea was to create an engine to provide an easy way to build CMS (Content Management System) applications or CMS modules for integration in applications.
It would be as easy as creating a business domain model (pojo classes) and marking them with special annotations, so all the functionality against business domain objects to view, search, modify with validation would be provided right away without a need to write any additional code.

Key technologies:
* Java 5
* Apache Struts 1.2.9
* JBoss (don't ask my why :-)) with Hibernate
* Apache HTTPD

This application consist of:
* Content Management Framework engine
* Business domain model for testing purposes

## Content Management Framework engine

Packages:
* **com.mks.domain** - CMF engine (domain tier classes)
  * **com.mks.domain.annotation** - annotations to use in business domain model classes
  * **com.mks.domain.util** - CMF engine (core classes)
* **com.mks.service** - CMF engine (service tier classes)
  * **EntityService** - service interface with available operations
* **com.mks.app** - CMF engine (web tier classes, Struts based)

## Business domain model for testing purposes

Packages:
* **com.mks.domain.discount**
* **com.mks.domain.offer**
