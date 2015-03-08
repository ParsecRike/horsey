package com.rikinator.configuration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by rike on 08/03/15.
 */
public class CustomThymeleafViewResolver extends ThymeleafViewResolver {

    public String templateDir; // Prefix
    public String fileType; // Suffix

    /**
     * Stash away the suffix (template directory) and suffix (file-type).
     * This data is also in the Template Engine but is not available when
     * this method is called (the engine is not initialised).
     *
     * @param templateDir
     * @param fileType
     */
    public CustomThymeleafViewResolver(String templateDir, String fileType) {
        this.fileType = fileType;
        this.templateDir = templateDir;
    }

    /**
     * Override to actually look for the resource to see if it
     * exists. This view resolver caches its views after the first time, so
     * the overhead of this check may be acceptible.
     */
    @Override
    protected boolean canHandle(final String viewName, final Locale locale) {
        Boolean exists = thymeleafViewExists(viewName,
                templateDir, fileType);
        // Fallback on other approach.
        return exists ;
    }

    @Override
    protected View createView(final String viewName, final Locale locale) throws Exception {
        if (!canHandle(viewName, locale)) {
            return null;
        } else {
            return super.createView(viewName, locale);
        }
    }

    protected Boolean thymeleafViewExists(String viewName, String prefix,
                                          String suffix) {
        String viewPath = prefix + viewName + suffix;
        logger.info("Checking view: " + viewName + " looking for " + viewPath);

        Resource res = null;


        boolean exists = false;
        if (viewPath.startsWith("classpath:"))
            res = new ClassPathResource(viewPath.substring(10));
        else if (viewPath.startsWith("file:"))
            res = new FileSystemResource(viewPath.substring(5));
        else {
            exists = getResource(viewPath);
        }

        if (!exists) {
            logger.info(viewPath + " not found, skipping Thymeleaf");
            return Boolean.FALSE;
        }

        logger.info("Found " + viewName + ", using Thymeleaf");
        return Boolean.TRUE;
    }

    private boolean getResource(String viewPath)  {
        Resource res;
        ServletContext context = getServletContext();
        URL url = null;
        try {
            url = context.getResource(viewPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File f = new File(url != null ? url.getPath() : "");
        return f.exists();
    }
}