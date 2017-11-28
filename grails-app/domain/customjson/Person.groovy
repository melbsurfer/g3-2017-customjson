package customjson

import grails.rest.Resource

@Resource(uri='/people')
class Person {
    String firstName
    String lastName
}
