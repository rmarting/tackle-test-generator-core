/*
Copyright IBM Corporation 2021

Licensed under the Eclipse Public License 2.0, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.konveyor.tackle.testgen.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.konveyor.tackle.testgen.TestUtils;
import org.konveyor.tackle.testgen.util.Constants;
import org.konveyor.tackle.testgen.util.TackleTestJson;
import org.konveyor.tackle.testgen.util.TackleTestLogger;
import org.konveyor.tackle.testgen.util.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BasicBlockSequenceParseTest {

	private static final Logger logger = TackleTestLogger.getLogger(BasicBlockSequenceParseTest.class);
	
	private final static ObjectMapper mapper = TackleTestJson.getObjectMapper();

	@Test
	public void parseRunner() throws Throwable {

		String projectClasspath = "";

		File file = new File("test/data/daytrader7/daytrader7MonoClasspath.txt");
		if (!file.isFile()) {
			throw new IOException(file.getAbsolutePath() + " is not a valid file");
		}
		projectClasspath += Utils.entriesToClasspath(Utils.getClasspathEntries(file));
		projectClasspath += (File.pathSeparator + "test/data/daytrader7/monolith/bin");

        projectClasspath += (File.pathSeparator + Utils.getJarPath(Constants.EVOSUITE_MASTER_JAR_NAME));
        projectClasspath += (File.pathSeparator + Utils.getJarPath(Constants.EVOSUITE_RUNTIME_JAR_NAME));
		// For SequenceExecutor class:
		projectClasspath += (File.pathSeparator + System.getProperty("java.class.path"));

		List<String> args = new ArrayList<String>();
		args.add("java");
		args.add("-cp");
		args.add("\"" + projectClasspath + "\""); // add double quotes in case path contains spaces
        // add jacoco agent argument to collect coverage data for process
        args.add(TestUtils.getJacocoAgentArg(BasicBlockSequenceParseTest.class.getSimpleName()));
        args.add(ParseRunner.class.getName());

		ProcessBuilder seuqenceParserPB = new ProcessBuilder(args);
		seuqenceParserPB.inheritIO();
		Process sequenceExecutorP = seuqenceParserPB.start();
		sequenceExecutorP.waitFor();
	}

	static class ParseRunner {

		private int totalInitSequences = 0;
		private int parsedInitSequences = 0;

		public void parseSequences() throws Throwable {

			ObjectNode initialTestSeq = (ObjectNode) mapper.readTree(new File("test/data/daytrader7/bb_sequences_test.json")).get("test_sequences");
			
			initialTestSeq.fieldNames().forEachRemaining(cls -> {

				ObjectNode clsInfo = (ObjectNode) initialTestSeq.get(cls);
				ArrayNode sequences = (ArrayNode) clsInfo.get("sequences");
				ArrayNode imports = (ArrayNode) clsInfo.get("imports");
				List<String> importList = mapper.convertValue(imports, new TypeReference<List<String>>(){});
						
				totalInitSequences += sequences.size();
				logger.info("Initial sequences for " + cls + ": " + sequences.size());
				logger.info("Imports: " + importList);

				// iterate over each string sequence for class and parse it into a randoop
				// sequence object
				
				sequences.elements().forEachRemaining(seq -> {
					
					String testSeq = seq.asText();
					
					// logger.fine("- " + testSeq);
					try {
						// create randoop sequence object
						SequenceParser.codeToSequence(testSeq, importList, cls, true, new ArrayList<Integer>());
						// logger.fine("Randoop test sequence: " + randoopSeq);
						parsedInitSequences++;

					} catch (Throwable e) {
						// if exception occurs in creating randoop sequence, record exception
						// information
						// for debugging
						logger.warning("Error parsing sequence for class " + cls + ":\n" + testSeq);
						e.printStackTrace();
						//throw e;
					}
					
				});
				
			});
			logger.info("=======> total_seq=" + totalInitSequences + "; parsed_seq="
					+ parsedInitSequences);

			assertEquals(totalInitSequences, parsedInitSequences);
		}

		public static void main(String[] args) throws Throwable {
			(new ParseRunner()).parseSequences();
		}

	}

}
