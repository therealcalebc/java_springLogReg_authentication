/**
 * 
 */
package cd.java.spring.logreg.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cd.java.spring.logreg.authentication.models.User;
import cd.java.spring.logreg.authentication.services.UserService;
import cd.java.spring.logreg.authentication.validators.UserValidator;

/**
 * @author ccomstock
 *
 */
@Controller
public class UsersController {
	
	@Autowired
    private UserService userService;
	@Autowired
	private UserValidator userValidator;
    
    @GetMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }
    @GetMapping("/login")
    public String login() {
        return "loginPage.jsp";
    }
    
    @PostMapping(value="/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
        // if result has errors, return the registration page (don't worry about validations just now)
        // else, save the user in the database, save the user id in session, and redirect them to the /home route
    	userValidator.validate(user, result);
    	if(result.hasErrors()) return "registrationPage.jsp";
    	User u = userService.registerUser(user);
    	session.setAttribute("userId", u.getId());
    	return "redirect:/home";
    }
    
    @PostMapping(value="/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes, HttpSession session) {
        // if the user is authenticated, save their user id in session
        // else, add error messages and return the login page
    	Long userId = userService.authenticateUser(email, password);
    	if(userId == null) {
//    		model.addAttribute("error", "Email address or password not found");
//    		return "loginPage.jsp";
            redirectAttributes.addFlashAttribute("error", "Email address or password not found");
            return "redirect:/login";
    	}
    	session.setAttribute("userId", userId);
    	return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // get user from session, save them in the model and return the home page
    	Long userId = (Long)session.getAttribute("userId");
    	if(userId == null) return "redirect:/";
    	User user = userService.findUserById(userId);
    	model.addAttribute("user", user);
    	return "homePage.jsp";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session
        // redirect to login page
    	session.invalidate();
    	return "redirect:/";
    }
    
}
