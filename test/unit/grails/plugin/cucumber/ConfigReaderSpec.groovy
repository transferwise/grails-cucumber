/*
 * Copyright 2012, 2014 Martin Hauner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.cucumber

import org.apache.log4j.*
import spock.lang.Specification


class ConfigReaderSpec extends Specification {
    def configFile
    def configSlurper
    def configObject

    def setupSpec () {
        // avoid log4j warning
        Logger root = Logger.getRootLogger();
        if (!root.getAllAppenders().hasMoreElements()) {
            root.setLevel (Level.INFO);
            root.addAppender (new ConsoleAppender ());
        }
    }

    def setup () {
        configFile = Mock (File)
        configSlurper = Mock (ConfigSlurper)

        configObject = new ConfigObject ()
        configObject.tags = ["@tag1", "@tag2"]
    }


    def "returns config object if config file does exist" () {
        given:
        configFile.exists () >> true
        configFile.toURI () >> new URI ('file://dummy')
        configSlurper.parse (_) >> configObject
        def reader = new ConfigReader (configFile, configSlurper)

        when:
        def config = reader.parse ()

        then:
        config.tags == ["@tag1", "@tag2"]
    }

    def "returns empty config object if config file does not exist" () {
        given:
        configFile.exists () >> false
        def reader = new ConfigReader (configFile, null)

        when:
        def config = reader.parse ()

        then:
        config.isEmpty ()
    }
}
