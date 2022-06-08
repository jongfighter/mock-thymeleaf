package com.example.demo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.support.RequestContext
import org.springframework.web.servlet.view.AbstractTemplateView
import org.thymeleaf.context.WebExpressionContext
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.context.webmvc.SpringWebMvcThymeleafRequestContext
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext
import org.thymeleaf.spring5.naming.SpringContextVariableNames
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet


@Controller
@RequestMapping("/api/test")
class Controller {

    @Autowired
    private lateinit var applicationContext:ApplicationContext

    @Autowired
    private lateinit var templateEngine:SpringTemplateEngine;
    @ResponseBody
    @GetMapping("")
    fun test(session:HttpSession, request:HttpServletRequest, response:HttpServletResponse):ResponseEntity<String> {
        session.setAttribute("kimchi","I love kimchi")
        var configuration = templateEngine.configuration;
        templateEngine.enableSpringELCompiler=true
        var mergedModel: MutableMap<String, Any> = HashMap(30)
        mergedModel["delete"]= "summer"
        val evaluationContext = ThymeleafEvaluationContext(applicationContext, null)
        // For compatibility with ThymeleafView
        val requestContext = RequestContext(request, response, request.servletContext, mergedModel)


        addRequestContextAsVariable(mergedModel, SpringContextVariableNames.SPRING_REQUEST_CONTEXT, requestContext)

        addRequestContextAsVariable(mergedModel, AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, requestContext)

        mergedModel[ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME] = evaluationContext

        val thymeleafRequestContext = SpringWebMvcThymeleafRequestContext(requestContext, request)
        mergedModel[SpringContextVariableNames.THYMELEAF_REQUEST_CONTEXT] = thymeleafRequestContext


        var webExpressionContext:WebExpressionContext = WebExpressionContext(configuration, request, response, request.servletContext, Locale.KOREA, mergedModel)
        // Set the Thymeleaf evaluation context to allow access to Spring beans with @beanName in SpEL expressions

        return ResponseEntity.ok().body(templateEngine.process("kimchi", webExpressionContext))

    }
    @GetMapping("/2")
    fun test2(session:HttpSession):ModelAndView {
        session.setAttribute("kimchi","I love kimchi")
        var modelAndView = ModelAndView();
        modelAndView.viewName = "kimchi";
        modelAndView.model["delete"] = "summer";
        return modelAndView;
    }
    @Throws(ServletException::class)
    fun addRequestContextAsVariable(
            model: MutableMap<String, Any>, variableName: String, requestContext: Any) {
        if (model.containsKey(variableName)) {
            throw ServletException(
                    "Cannot expose request context in model attribute '" + variableName +
                            "' because an existing model object of the same name")
        }
        model[variableName] = requestContext
    }
    @GetMapping("/json")
    @ResponseBody
    fun getJson(): ResponseEntity<String> {
        var objectMapper = ObjectMapper()
        var simpleModule = SimpleModule()
        simpleModule.addDeserializer(Menu::class,MenuDeserializer())
        objectMapper.registerModule(simpleModule)
        var classPathResource = ClassPathResource("static/test.json")
        var file = classPathResource.file
        var menu = objectMapper.readValue(file, object: TypeReference<LinkedHashSet<Menu>>(){})
        var s = objectMapper.writeValueAsString(menu)

        return ResponseEntity.ok().body(s.replace("\r\n", "<br>"))

    }
}