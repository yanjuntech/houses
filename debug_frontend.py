from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page()
    
    # Capture console messages
    console_messages = []
    page.on("console", lambda msg: console_messages.append(f"[{msg.type}] {msg.text}"))
    
    # Capture page errors
    page_errors = []
    page.on("pageerror", lambda err: page_errors.append(str(err)))
    
    # Navigate
    response = page.goto('http://localhost:8081/', wait_until='networkidle', timeout=30000)
    print(f"HTTP Status: {response.status}")
    
    # Wait a bit for Vue to render
    page.wait_for_timeout(3000)
    
    # Screenshot
    page.screenshot(path='/workspace/debug_frontend.png', full_page=True)
    
    # Get page content
    content = page.content()
    print(f"Page length: {len(content)} chars")
    print(f"Body text: {page.inner_text('body')[:500]}")
    
    # Check #app content
    app_html = page.eval_on_selector('#app', 'el => el.innerHTML')
    print(f"\n#app innerHTML length: {len(app_html)}")
    print(f"#app content (first 500): {app_html[:500]}")
    
    # Print console messages
    print(f"\n=== Console Messages ({len(console_messages)}) ===")
    for msg in console_messages:
        print(msg)
    
    # Print page errors
    print(f"\n=== Page Errors ({len(page_errors)}) ===")
    for err in page_errors:
        print(err)
    
    browser.close()
