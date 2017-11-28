package customjson

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Integration
@Stepwise
class CustomJsonPersonFunctionalSpec extends Specification {

    @Shared
    def rest = new RestBuilder()

    void "test that no people exist"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/people")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 0
    }

    void "test creating people"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/people") {
            json {
                firstName = 'Alex'
                lastName = 'Lifeson'
            }
        }
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.surname == 'Lifeson'
        resp.json.givenName == 'Alex'

        when:
        resp = rest.post("http://localhost:${serverPort}/people") {
            json {
                firstName = 'Geddy'
                lastName = 'Lee'
            }
        }

        then:
        resp.status == HttpStatus.CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.surname == 'Lee'
        resp.json.givenName == 'Geddy'

        when:
        resp = rest.post("http://localhost:${serverPort}/people") {
            json {
                firstName = 'Neil'
                lastName = 'Peart'
            }
        }

        then:
        resp.status == HttpStatus.CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.surname == 'Peart'
        resp.json.givenName == 'Neil'
    }

    void 'test retrieving list of people defaults to JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/people")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 3

        and:
        resp.json[0].surname == 'Lifeson'
        resp.json[0].givenName == 'Alex'

        and:
        resp.json[1].surname == 'Lee'
        resp.json[1].givenName == 'Geddy'

        and:
        resp.json[2].surname == 'Peart'
        resp.json[2].givenName == 'Neil'
    }
}
