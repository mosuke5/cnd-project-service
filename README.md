![status](https://travis-ci.org/mosuke5/cnd-project-service.svg?branch=master)

# Freelancer4j project service
Freelancer4j project service for Appmod Microservices Advanced course.

Implementation: Vert.x + MongoDB

## How to test
Test codes start mongodb before execution of test. It's not mock database.

```
$ mvn clean test
Results :

Tests run: 13, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 9.348 s
[INFO] Finished at: 2019-06-02T22:38:53+09:00
[INFO] ------------------------------------------------------------------------
```

## Continuous Integration in Travis CI
This repository is integrated with Travis CI.
When you push to github, Travis CI will test automatically.

Current status is here.
https://travis-ci.org/mosuke5/cnd-project-service

## How to deploy to OpneShift
```
$ export FREELANCER4J_PRJ=<your OpenShift project>

// Deplpy mongodb to OpenShift
$ oc process -f etc/freelancer4j-project-mongodb-persistent.yaml \
-p PROJECT_DB_USERNAME=mongo \
-p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $FREELANCER4J_PRJ

// Create configmap for this service
$ oc create configmap project-service --from-file=etc/app-config.yml -n $FREELANCER4J_PRJ

// Deploy this service to OpenShift
$ mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$FREELANCER4J_PRJ
```

## Functions
| Method | Endpoint |
|:-----------|:------------|
| GET | /projects |
| GET | /projects/{projectId} |
| GET | /projects/status/{projectStatus} |

```
$ export PROJECT_URL=http://$(oc get route project-service -n $FREELANCER4J_PRJ -o template --template='{{.spec.host}}')

$ curl -X GET "$PROJECT_URL/projects"
[
  {
    "projectId": "329291",
    "ownerFirstName": "Mai",
    "ownerLastName": "Oyama",
    "ownerEmail": "mai.oyama@exmaple.com",
    "projectTitle": "project-1",
    "projectDescription": "project-1-desc",
    "projectStatus": "open"
  },
  {
    "projectId": "329292",
    "ownerFirstName": "Seiko",
    "ownerLastName": "Kotani",
    "ownerEmail": "seiko.kotani@exmaple.com",
    "projectTitle": "project-2",
    "projectDescription": "project-2-desc",
    "projectStatus": "open"
  },
  ...
]

$ curl -X GET "$PROJECT_URL/projects/329291"
{
  "projectId": "329291",
  "ownerFirstName": "Mai",
  "ownerLastName": "Oyama",
  "ownerEmail": "mai.oyama@exmaple.com",
  "projectTitle": "project-1",
  "projectDescription": "project-1-desc",
  "projectStatus": "open"
}


$ curl -X GET "$PROJECT_URL/projects/status/open"
[
  {
    "projectId": "329291",
    "ownerFirstName": "Mai",
    "ownerLastName": "Oyama",
    "ownerEmail": "mai.oyama@exmaple.com",
    "projectTitle": "project-1",
    "projectDescription": "project-1-desc",
    "projectStatus": "open"
  },
  {
    "projectId": "329292",
    "ownerFirstName": "Seiko",
    "ownerLastName": "Kotani",
    "ownerEmail": "seiko.kotani@exmaple.com",
    "projectTitle": "project-2",
    "projectDescription": "project-2-desc",
    "projectStatus": "open"
  }
]
```
