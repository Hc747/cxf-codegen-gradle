/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mateo.cxf.codegen.wsdl2java;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.List;

import io.mateo.cxf.codegen.junit.TaskNameGenerator;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

@DisplayNameGeneration(TaskNameGenerator.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Wsdl2JavaOptionsTests {

	private Project project;

	private File projectDir;

	private File outputDir;

	private File wsdl;

	@BeforeAll
	void beforeAll(@TempDir File projectDir, @TempDir File wsdl) {
		this.projectDir = projectDir;
		this.project = ProjectBuilder.builder().withProjectDir(projectDir).build();
		this.outputDir = this.project.getLayout().getBuildDirectory().get().getAsFile();
		this.wsdl = wsdl;
	}

	@AfterEach
	void tearDown(TestInfo testInfo) {
		this.project.getTasks().named(testInfo.getDisplayName(), task -> task.setEnabled(false));
	}

	@Test
	void packageNames(TestInfo testInfo) {
		List<String> expected = List.of("-p", "com.example.foo", "-p", "com.example.bar", "-d",
				getOutputDirFor(testInfo), this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getPackageNames().set(List.of("com.example.foo", "com.example.bar")))
						.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void namespaceExcludes(TestInfo testInfo) {
		List<String> expected = List.of("-nexclude", "foo", "-nexclude", "bar", "-d", getOutputDirFor(testInfo),
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getNamespaceExcludes().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void bindingFiles(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-b",
				new File(this.projectDir, "foo").toURI().toString(), "-b",
				new File(this.projectDir, "bar").toURI().toString(), this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getBindingFiles().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void frontend(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-fe", "foo", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getFrontend().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void databinding(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-db", "foo", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getDatabinding().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void wsdlVersion(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-wv", "foo", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getWsdlVersion().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void catalog(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-catalog", "foo",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getCatalog().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void extendedSoapHeaders(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-exsh", "true", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getExtendedSoapHeaders().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void noTypes(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-noTypes", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getNoTypes().set(true))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void allowElementRefs(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-allowElementReferences",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getAllowElementRefs().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void validateWsdl(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-validate=foo", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getValidateWsdl().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void markGenerated(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-mark-generated",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getMarkGenerated().set(true))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void suppressGeneratedDate(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-suppress-generated-date",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getSuppressGeneratedDate().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void defaultExcludesNamespace(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-dex", "true", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getDefaultExcludesNamespace().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void defaultNamespacePackingMapping(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-dns", "true", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getDefaultNamespacePackageMapping().set(true)).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void serviceName(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-sn", "foo", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getServiceName().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void faultSerial(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-faultSerialVersionUID", "1234",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getFaultSerialVersionUid().set("1234")).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void exceptionSuper(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-exceptionSuper",
				UncheckedIOException.class.toString(), this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getExceptionSuper().set(UncheckedIOException.class.toString()))
						.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void seiSuper(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-seiSuper", "foo", "-seiSuper", "bar",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getSeiSuper().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void autoNameResolution(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-autoNameResolution",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getAutoNameResolution().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void noAddressBinding(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-noAddressBinding",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getNoAddressBinding().set(true)).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void xjcArgs(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-xjc-Xts", "-xjc-Xwsdlextension",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getXjcArgs().set(List.of("-Xts", "-Xwsdlextension"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void extraArgs(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-client", "-all",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getExtraArgs().set(List.of("-client", "-all"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void wsdlLocation(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-wsdlLocation", "foo",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getWsdlLocation().set("foo"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void wsdlList(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-wsdlList", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getWsdlList().set(true))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void encoding(TestInfo testInfo) {
		List<String> expected = List.of("-encoding", "UTF-8", "-d", getOutputDirFor(testInfo),
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getEncoding().set("UTF-8"))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void verbose(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-verbose", this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(), options -> options.getVerbose().set(true))
				.getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void asyncMethods(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-asyncMethods=foo,bar",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getAsyncMethods().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void bareMethods(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-bareMethods=foo,bar",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getBareMethods().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void mimeMethods(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), "-mimeMethods=foo,bar",
				this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName(),
				options -> options.getMimeMethods().set(List.of("foo", "bar"))).getArgumentProviders().get(0)
						.asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@Test
	void wsdlOnly(TestInfo testInfo) {
		List<String> expected = List.of("-d", getOutputDirFor(testInfo), this.wsdl.toURI().toString());

		Iterable<String> actual = createTask(testInfo.getDisplayName()).getArgumentProviders().get(0).asArguments();

		assertThat(actual).containsExactlyElementsOf(expected);
	}

	private String getOutputDirFor(TestInfo testInfo) {
		return String.format("%s/%s-wsdl2java-generated-sources", this.outputDir.getAbsolutePath(),
				testInfo.getDisplayName());
	}

	private Wsdl2Java createTask(String taskName, Action<? super Wsdl2JavaOptions> configurer) {
		return this.project.getTasks().create(taskName, Wsdl2Java.class, wsdl2java -> wsdl2java.toolOptions(options -> {
			options.getWsdl().set(this.wsdl);
			configurer.execute(options);
		}));
	}

	private Wsdl2Java createTask(String taskName) {
		return this.project.getTasks().create(taskName, Wsdl2Java.class,
				wsdl2java -> wsdl2java.toolOptions(options -> options.getWsdl().set(this.wsdl)));
	}

}
