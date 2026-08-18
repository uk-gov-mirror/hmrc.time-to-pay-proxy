import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings

val appName = "time-to-pay-proxy"

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.3.8"

val silencerVersion = "1.7.3"
lazy val ItTest = config("it") extend Test
lazy val coverageSettings: Seq[Setting[_]] =
  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;.*Module.*;.*AuthService.*;models\\.data\\..*;uk.gov.hmrc.BuildInfo;app.*;nr.*;res.*;prod.*;.*RuleAST.*;config.*;testOnlyDoNotUseInAppConf.*;definition.*;.*FeatureSwitch.*",
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    dependencyOverrides ++= AppDependencies.dependencyOverrides,
    scalacOptions ++= Seq(
      "-source:future-migration",
      "-Xfatal-warnings",
      "-Wunused:implicits", // Replaced -Ywarn-unused:implicits
      "-Wunused:imports", // Replaced -Ywarn-unused:imports
      "-Wunused:locals", // Replaced -Ywarn-unused:locals
      "-Wunused:params", // Replaced -Ywarn-unused:params
      "-Wunused:privates", // Replaced -Ywarn-unused:privates
      "-language:higherKinds",
      "-Wconf:src=routes/.*:s",
      "-Wconf:src=html/.*:s",
      "-Wconf:msg=Flag.*repeatedly:s",
      "-Wconf:msg=.*Manifest.*:s", // Suppresses the Manifest deprecation error
      "-feature"
    )
  )
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources"
  )
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(coverageSettings *)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    Compile / doc / scalacOptions ++= Seq(
      "-no-link-warnings" // Suppresses problems with Scaladoc @throws links
    )
  )

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(DefaultBuildSettings.itSettings())
  .settings(
    Compile / doc / scalacOptions ++= Seq(
      "-no-link-warnings" // Suppresses problems with Scaladoc @throws links
    )
  )
