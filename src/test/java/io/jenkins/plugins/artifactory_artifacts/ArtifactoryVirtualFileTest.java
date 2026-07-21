package io.jenkins.plugins.artifactory_artifacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * Tests for ArtifactoryVirtualFile.toExternalURL() behavior controlled by disableDirectDownload.
 */
@WithJenkins
@WireMockTest
public class ArtifactoryVirtualFileTest extends BaseTest {

    @Test
    public void toExternalUrlShouldReturnArtifactoryUrlWhenDisableDirectDownloadIsFalse(
            JenkinsRule jenkinsRule, WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        ArtifactoryGenericArtifactConfig config = configureConfig(jenkinsRule, wmRuntimeInfo.getHttpPort(), "");
        config.setDisableDirectDownload(false);

        ArtifactoryVirtualFile virtualFile =
                new ArtifactoryVirtualFile("my-generic-repo/job/1/artifacts/file.txt", null);

        URL url = virtualFile.toExternalURL();

        assertThat("toExternalURL() should return a URL when disableDirectDownload is false", url, notNullValue());
        assertThat(
                "URL should point to the configured Artifactory server",
                url.toString().startsWith("http://localhost:" + wmRuntimeInfo.getHttpPort()),
                equalTo(true));
    }

    @Test
    public void toExternalUrlShouldReturnNullWhenDisableDirectDownloadIsTrue(
            JenkinsRule jenkinsRule, WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        ArtifactoryGenericArtifactConfig config = configureConfig(jenkinsRule, wmRuntimeInfo.getHttpPort(), "");
        config.setDisableDirectDownload(true);

        ArtifactoryVirtualFile virtualFile =
                new ArtifactoryVirtualFile("my-generic-repo/job/1/artifacts/file.txt", null);

        URL url = virtualFile.toExternalURL();

        assertThat("toExternalURL() should return null when disableDirectDownload is true", url, nullValue());
    }
}
