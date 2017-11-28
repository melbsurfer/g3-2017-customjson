package customjson

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Integration
@Stepwise
class CustomJsonCarFunctionalSpec extends Specification {

    @Shared
    def rest = new RestBuilder()

    void "test that no cars exist"() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 0
    }

    void "test creating cars"() {
        when:
        def resp = rest.post("http://localhost:${serverPort}/automobiles") {
            json {
                make = 'Chevy'
                model = 'Equinox'
            }
        }
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.manufacturer == 'Chevy'
        resp.json.series == 'Equinox'

        when:
        resp = rest.post("http://localhost:${serverPort}/automobiles") {
            json {
                make = 'Ford'
                model = 'Fusion'
            }
        }

        then:
        resp.status == HttpStatus.CREATED.value()
        contentType.subtype == 'json'
        contentType.type == 'application'

        and:
        resp.json.manufacturer == 'Ford'
        resp.json.series == 'Fusion'
    }

    void 'test retrieving list of cars defaults to JSON'() {
        when:
        def resp = rest.get("http://localhost:${serverPort}/automobiles")
        def contentType = resp.headers.getContentType()

        then:
        resp.status == HttpStatus.OK.value()
        contentType.subtype == 'json'
        contentType.type == 'application'
        resp.json.size() == 2

        and:
        resp.json[0].manufacturer == 'Chevy'
        resp.json[0].series == 'Equinox'

        and:
        resp.json[1].manufacturer == 'Ford'
        resp.json[1].series == 'Fusion'
    }
}
