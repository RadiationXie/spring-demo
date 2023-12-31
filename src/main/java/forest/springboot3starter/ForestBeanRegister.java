package forest.springboot3starter;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.config.SpringForestProperties;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.converter.auto.DefaultAutoConverter;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.interceptor.SpringInterceptorFactory;
import com.dtflys.forest.logging.ForestLogHandler;
import com.dtflys.forest.reflection.SpringForestObjectFactory;
import com.dtflys.forest.spring.ConverterBeanListener;
import com.dtflys.forest.springboot.properties.ForestConfigurationProperties;
import com.dtflys.forest.springboot.properties.ForestConvertProperties;
import com.dtflys.forest.springboot.properties.ForestConverterItemProperties;
import com.dtflys.forest.springboot.properties.ForestSSLKeyStoreProperties;
import com.dtflys.forest.utils.ForestDataType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class ForestBeanRegister implements ResourceLoaderAware, BeanPostProcessor {

  private final ConfigurableApplicationContext applicationContext;
  private ResourceLoader resourceLoader;
  private SpringForestProperties forestProperties;
  private SpringForestObjectFactory forestObjectFactory;
  private SpringInterceptorFactory forestInterceptorFactory;
  private ForestConfigurationProperties forestConfigurationProperties;

  public ForestBeanRegister(
    ConfigurableApplicationContext applicationContext,
    ForestConfigurationProperties forestConfigurationProperties,
    SpringForestProperties properties,
    SpringForestObjectFactory forestObjectFactory,
    SpringInterceptorFactory forestInterceptorFactory) {
    this.applicationContext = applicationContext;
    this.forestProperties = properties;
    this.forestObjectFactory = forestObjectFactory;
    this.forestConfigurationProperties = forestConfigurationProperties;
    this.forestInterceptorFactory = forestInterceptorFactory;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public ForestConfiguration registerForestConfiguration() {
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ForestConfiguration.class);
    String id = forestConfigurationProperties.getBeanId();
    if (StringUtils.isBlank(id)) {
      id = "forestConfiguration";
    }
    Class<? extends ForestLogHandler> logHandlerClass = forestConfigurationProperties.getLogHandler();
    ForestLogHandler logHandler = null;
    if (logHandlerClass != null) {
      try {
        logHandler = logHandlerClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
        throw new ForestRuntimeException(e);
      }
    }

    beanDefinitionBuilder
      .addPropertyValue("maxAsyncThreadSize", forestConfigurationProperties.getMaxAsyncThreadSize())
      .addPropertyValue("maxAsyncQueueSize", forestConfigurationProperties.getMaxAsyncQueueSize())
      .addPropertyValue("maxConnections", forestConfigurationProperties.getMaxConnections())
      .addPropertyValue("maxRouteConnections", forestConfigurationProperties.getMaxRouteConnections())
      .addPropertyValue("asyncMode", forestConfigurationProperties.getAsyncMode())
      .addPropertyValue("timeout", forestConfigurationProperties.getTimeout())
      .addPropertyValue("connectTimeout", forestConfigurationProperties.getConnectTimeoutMillis())
      .addPropertyValue("readTimeout", forestConfigurationProperties.getReadTimeoutMillis())
      .addPropertyValue("charset", forestConfigurationProperties.getCharset())
      .addPropertyValue("retryer", forestConfigurationProperties.getRetryer())
      .addPropertyValue("maxRetryCount", forestConfigurationProperties.getMaxRetryCount())
      .addPropertyValue("maxRetryInterval", forestConfigurationProperties.getMaxRetryInterval())
      .addPropertyValue("autoRedirection", forestConfigurationProperties.isAutoRedirection())
      .addPropertyValue("logEnabled", forestConfigurationProperties.isLogEnabled())
      .addPropertyValue("logRequest", forestConfigurationProperties.isLogRequest())
      .addPropertyValue("logResponseStatus", forestConfigurationProperties.isLogResponseStatus())
      .addPropertyValue("logResponseContent", forestConfigurationProperties.isLogResponseContent())
      .addPropertyValue("logHandler", logHandler)
      .addPropertyValue("backendName", forestConfigurationProperties.getBackend())
      .addPropertyValue("baseAddressScheme", forestConfigurationProperties.getBaseAddressScheme())
      .addPropertyValue("baseAddressHost", forestConfigurationProperties.getBaseAddressHost())
      .addPropertyValue("baseAddressPort", forestConfigurationProperties.getBaseAddressPort())
      .addPropertyValue("baseAddressSourceClass", forestConfigurationProperties.getBaseAddressSource())
      .addPropertyValue("successWhenClass", forestConfigurationProperties.getSuccessWhen())
      .addPropertyValue("retryWhenClass", forestConfigurationProperties.getRetryWhen())
      .addPropertyValue("interceptors", forestConfigurationProperties.getInterceptors())
      .addPropertyValue("sslProtocol", forestConfigurationProperties.getSslProtocol())
      .addPropertyValue("variables", forestConfigurationProperties.getVariables())
      .setLazyInit(false)
      .setFactoryMethod("configuration")
      .addConstructorArgValue(id);

    List<ForestSSLKeyStoreProperties> sslKeyStorePropertiesList = forestConfigurationProperties.getSslKeyStores();
    ManagedMap<String, BeanDefinition> sslKeystoreMap = new ManagedMap<>();
    for (ForestSSLKeyStoreProperties keyStoreProperties : sslKeyStorePropertiesList) {
//      registerSSLKeyStoreBean(sslKeystoreMap, keyStoreProperties);
    }

    BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
    beanDefinition.getPropertyValues().addPropertyValue("sslKeyStores", sslKeystoreMap);

    BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
    beanFactory.registerBeanDefinition(id, beanDefinition);

    ForestConfiguration configuration = applicationContext.getBean(id, ForestConfiguration.class);
    configuration.setProperties(forestProperties);
    configuration.setForestObjectFactory(forestObjectFactory);
    configuration.setInterceptorFactory(forestInterceptorFactory);

    Map<String, Class> filters = forestConfigurationProperties.getFilters();
    for (Map.Entry<String, Class> entry : filters.entrySet()) {
      String filterName = entry.getKey();
      Class filterClass = entry.getValue();
      configuration.registerFilter(filterName, filterClass);
    }

    ForestConvertProperties convertProperties = forestConfigurationProperties.getConverters();
    if (convertProperties != null) {
      registerConverter(configuration, ForestDataType.TEXT, convertProperties.getText());
      registerConverter(configuration, ForestDataType.JSON, convertProperties.getJson());
      registerConverter(configuration, ForestDataType.XML, convertProperties.getXml());
      registerConverter(configuration, ForestDataType.BINARY, convertProperties.getBinary());
      registerConverter(configuration, ForestDataType.PROTOBUF, convertProperties.getProtobuf());
    }
    registerConverterBeanListener(configuration);
    return configuration;
  }

  public ConverterBeanListener registerConverterBeanListener(ForestConfiguration forestConfiguration) {
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ConverterBeanListener.class);
    BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
    beanDefinition.getPropertyValues().addPropertyValue("forestConfiguration", forestConfiguration);
    BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
    beanFactory.registerBeanDefinition("forestConverterBeanListener", beanDefinition);
    return applicationContext.getBean("forestConverterBeanListener", ConverterBeanListener.class);
  }

  private void registerConverter(
    ForestConfiguration configuration,
    ForestDataType dataType,
    ForestConverterItemProperties converterItemProperties
  ) {
    if (converterItemProperties == null) {
      return;
    }

    Class type = converterItemProperties.getType();
    if (type != null) {
      ForestConverter converter = null;
      try {
        Constructor<?>[] constructors = type.getConstructors();
        for (Constructor<?> constructor : constructors) {
          Parameter[] params = constructor.getParameters();
          if (params.length == 0) {
            converter = (ForestConverter) constructor.newInstance(new Object[0]);
            break;
          } else {
            Object[] args = new Object[params.length];
            Class[] pTypes = constructor.getParameterTypes();
            for (int i = 0;i < params.length;i++) {
              Class pType = pTypes[i];
              if (ForestConfiguration.class.isAssignableFrom(pType)) {
                args[i] = configuration;
              } else if (DefaultAutoConverter.class.isAssignableFrom(pType)) {
                args[i] = configuration.getConverter(ForestDataType.AUTO);
              }
            }
            converter = (ForestConverter) constructor.newInstance(args);
          }
        }
        Map<String, Object> parameters = converterItemProperties.getParameters();
        PropertyDescriptor[] descriptors = ReflectUtils.getBeanSetters(type);
        for (PropertyDescriptor descriptor : descriptors) {
          String name = descriptor.getName();
          Object value = parameters.get(name);
          Method method = descriptor.getWriteMethod();
          if (method != null) {
            try {
              method.invoke(converter, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
              throw new ForestRuntimeException("An error occurred during setting the property " + type.getName() + "." + name, e);
            }
          }
        }
        configuration.getConverterMap().put(dataType, converter);
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
}
