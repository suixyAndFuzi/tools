package com.example.demo.serverImpl.design.template;

/**
 * 模版模式
 *
 * @author fuzi
 */
public abstract class TemplateDemo {

    public final void a() {
        System.out.println("a");
        b();
        System.out.println("a end");
    }

    protected abstract void b();
}


class TemplateDemoB extends TemplateDemo {

    @Override
    protected void b() {
        System.out.println("b");
    }

    public static void main(String[] args) {
        TemplateDemo t = new TemplateDemoB();
        t.a();
    }
}
