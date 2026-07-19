"""
Step 1: Verify login and take initial screenshots.
"""
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    context = browser.new_context(viewport={"width": 1600, "height": 900})
    page = context.new_page()
    # capture console logs
    page.on("console", lambda msg: print(f"[CONSOLE.{msg.type}] {msg.text}"))
    page.on("pageerror", lambda exc: print(f"[PAGE ERROR] {exc}"))

    print("==> Navigating to login page")
    page.goto("http://localhost:8081/", wait_until="networkidle", timeout=60000)
    page.wait_for_timeout(1500)
    page.screenshot(path="/tmp/01_landing.png", full_page=True)
    print("URL after load:", page.url)
    print("Title:", page.title())

    # Try default RuoYi login admin/admin123
    try:
        page.wait_for_selector("input[placeholder*='请输入用户名']", timeout=10000)
        page.fill("input[placeholder*='请输入用户名']", "admin")
        page.fill("input[placeholder*='请输入密码']", "admin123")
        page.screenshot(path="/tmp/02_login_filled.png", full_page=True)
        page.click("button[type='button']:has-text('登录')")
        page.wait_for_timeout(5000)
        page.wait_for_load_state("networkidle", timeout=30000)
        print("After login URL:", page.url)
        page.screenshot(path="/tmp/03_after_login.png", full_page=True)
    except Exception as e:
        print(f"Login attempt failed: {e}")
        page.screenshot(path="/tmp/03_login_error.png", full_page=True)

    # Save storage state for subsequent scripts
    context.storage_state(path="/tmp/auth_state.json")
    print("==> Done")
    browser.close()
