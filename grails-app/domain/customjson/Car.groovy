package customjson

import grails.rest.Resource

@Resource(uri='/automobiles')
class Car {
    String make
    String model
}
