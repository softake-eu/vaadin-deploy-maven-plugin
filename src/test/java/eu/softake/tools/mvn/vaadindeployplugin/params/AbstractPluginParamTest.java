package eu.softake.tools.mvn.vaadindeployplugin.params;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPluginParamTest {

    @Test
    void pathDecorationAddsSlashToStart() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("hello/world/");
        System.out.println(serverParam.getDeployDir());
        assertTrue(serverParam.getDeployDir().startsWith("/"));
    }

    @Test
    void pathDecorationDoesntAddExtraSlashToStart() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("/hello/world");
        System.out.println(serverParam.getDeployDir());

        assertTrue(serverParam.getDeployDir().startsWith("/"));
        assertFalse(serverParam.getDeployDir().startsWith("//"));
    }

    @Test
    void pathDecorationTrimsSpaces() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir(" /hello/world/ ");
        System.out.println(serverParam.getDeployDir());

        assertTrue(serverParam.getDeployDir().startsWith("/"));
        assertFalse(serverParam.getDeployDir().endsWith("/"));
        assertFalse(serverParam.getDeployDir().endsWith(" "));
    }

    @Test
    void pathDecorationRemovesSlashFromEnd() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("hello/world/");
        System.out.println(serverParam.getDeployDir());

        assertFalse(serverParam.getDeployDir().endsWith("/"));
    }
}