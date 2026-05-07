package com.example.vaadin.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.icon.VaadinIcon;

@StyleSheet("styles.css")
@StyleSheet(Lumo.UTILITY_STYLESHEET)
public class MainLayout extends AppLayout {

    private H2 viewTitle;


    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addHeaderContent();
        addDrawerContent();
        addFooterContent();

    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Span userIdentifier = new Span("Käyttäjä: Admin");
        userIdentifier.addClassNames(LumoUtility.Margin.Left.AUTO, LumoUtility.FontSize.SMALL);

        Button logout = new Button("Kirjaudu ulos", VaadinIcon.SIGN_OUT.create(), e -> {});
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Header header = new Header(toggle, viewTitle, userIdentifier, logout);
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);

        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Projektin hallinta");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Padding.MEDIUM);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Työntekijät", EmployeeView.class, VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Osastot", DepartmentView.class, VaadinIcon.BUILDING.create()));
        nav.addItem(new SideNavItem("Sopimus", ContractView.class, VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Projekti", ProjectView.class, VaadinIcon.TASKS.create()));

        Scroller scroller = new Scroller(nav);
        addToDrawer(appName, scroller);
    }

    private void addFooterContent() {
        Footer footer = new Footer();

        footer.getStyle()
                .set("position", "fixed")
                .set("bottom", "0")
                .set("left", "0")
                .set("right", "0")
                .set("z-index", "10");

        footer.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.Padding.SMALL,
                LumoUtility.Background.CONTRAST_10,
                LumoUtility.Gap.MEDIUM,
                LumoUtility.FontSize.SMALL
        );

        Span copyright = new Span("© 2026 " + "Kasper Tuomainen ");
        Anchor link = new Anchor("https://google.fi", "Linkki");
        link.addClassName(LumoUtility.TextColor.SECONDARY);

        footer.add(copyright, link);
        getElement().appendChild(footer.getElement());
    }
}