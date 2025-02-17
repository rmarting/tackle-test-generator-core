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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.konveyor.tackle.testgen.util.TackleTestLogger;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Reads the junit tests created by the test generator and extracts their code sequences
 * @author RACHELBRILL
 *
 */

public abstract class AbstractJUnitTestImporter {

	protected Set<File> junitFiles = new HashSet<File>();

	private Map<String, Set<String>> classToSequences = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> classToImports = new HashMap<String, Set<String>>();

    protected File rootDir;

	public abstract String getClassName(File testFile) throws FileNotFoundException;

	public abstract boolean isJUnitFile(File testFile);

	protected abstract void getTests(File dir) throws IOException;

	protected abstract String filterSequence(String seq);

	protected abstract List<String> beforeAfterCodeSegments(String className)  throws IOException;

	protected abstract void compileBeforeAfterCode(List<String> classes, String classpath);
	
	private static final Logger logger = TackleTestLogger.getLogger(AbstractJUnitTestImporter.class);

	AbstractJUnitTestImporter(File dir) {

		rootDir = dir;
	}

	void importSequences() throws IOException {

		if ( ! rootDir.isDirectory()) {
			throw new IOException("Test generator failed to generate any tests");
		}

		getTests(rootDir);
		getSequences();
	}

	private void getSequences() throws IOException {

		for (File file : junitFiles) {

			getClassInfo(file);
		}
	}

	private void getClassInfo(File file) throws IOException {

		String className = getClassName(file);

		Set<String> sequences = new HashSet<String>();
		Set<String> imports = new HashSet<String>();

		logger.info("Parsing file "+file.getName());

		CompilationUnit compUnit = StaticJavaParser.parse(file);

		compUnit.findAll(MethodDeclaration.class).stream().forEach(c -> {
			String methodBody = c.getBody().get().toString();
			methodBody = filterSequence(methodBody);
			sequences.add(methodBody.toString());
		});

		compUnit.findAll(ImportDeclaration.class).stream().forEach(c -> {
			imports.add((c.isStatic()? "static " : "") + c.getNameAsString() + (c.isAsterisk()? ".*" : ""));
		});

		// add an import for the diff assertions that we may add later
        imports.add("static org.junit.Assert.assertEquals");
        imports.add("static org.junit.Assert.assertNull");

		classToSequences.put(className, sequences);
		classToImports.put(className, imports);
	}

	public Set<String> getSequences(String className) {
		return classToSequences.get(className);
	}

	public Set<String> getImports(String className) {
		return classToImports.get(className);
	}

}
