.PHONY: help spotless clean-build compile compilation-check integration-test check dev-local all

help:
	@echo "Available targets:"
	@echo "  spotless            - Run Spotless code formatting check"
	@echo "  compile             - Compile all code (production, tests, integration tests, test fixtures)"
	@echo "  integration-test    - Run integration tests"
	@echo "  clean-build         - Clean and build the project"
	@echo "  check               - Run spotless check and clean build"
	@echo "  dev-local           - Build and run dev-local in one shot (re-run on every code change)"

spotless:
	./gradlew spotlessKotlinApply

compile:
	./gradlew compileKotlin compileTestKotlin compileTestFixturesKotlin --console=plain

integration-test:
	./gradlew integrationTest

clean-build:
	./gradlew clean build

.DEFAULT_GOAL := all
