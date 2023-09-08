package spring.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResourceDependencyInjection {
    private Resource xmlFileInjectResource;
    private Resource[] resources;
    private Resource resourceValueAnnotation;

    public ResourceDependencyInjection(@Value("${templates.path}") Resource[] resources, @Value("${template.path}") Resource resource) {
        this.resources = resources;
        this.resourceValueAnnotation = resource;
    }

    public void setXmlFileInjectResource(Resource xmlFileInjectResource) { this.xmlFileInjectResource = xmlFileInjectResource; }
    public void printResourceInfo() {
        try {

            System.out.println("\n========================================分割线==============================================");
            printResourceInterfaceMethod(xmlFileInjectResource);
            printResourceInterfaceMethod(resourceValueAnnotation);

            BufferedInputStream in = new BufferedInputStream(xmlFileInjectResource.getInputStream());

            int bytesReader;
            byte[] buff = new byte[1024];
            while ((bytesReader = in.read(buff)) != -1) System.out.println(new String(buff, 0, bytesReader));

            System.out.println("类路径templates文件夹的所有txt文件：" + Arrays.toString(resources));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void printResourceInterfaceMethod(Resource resource) throws IOException {
        System.out.print("Description: " + resource.getDescription() + ", ");
        System.out.print("Filename: " + resource.getFilename() + ", ");
        System.out.print("URI_Path: " + resource.getURI().getPath() + ", ");
        System.out.println("ContentLength: " + resource.contentLength());
    }
}
