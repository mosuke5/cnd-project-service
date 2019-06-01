![status](https://travis-ci.org/mosuke5/cnd-project-service.svg?branch=master)

Freelancer4j project service for Appmod Microservices Advanced course.

Implementation: Vert.x + MongoDB

```
$ oc process -f etc/freelancer4j-project-mongodb-persistent.yaml \
-p PROJECT_DB_USERNAME=mongo \
-p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $FREELANCER4J_PRJ

$ oc create configmap project-service --from-file=etc/app-config.yml -n $FREELANCER4J_PRJ
```