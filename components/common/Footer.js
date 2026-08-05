import "./Footer.css";

function Footer() {
  return (
    <footer id="contact" className="footer">

      <div className="container">

        <div className="row gy-4">

          {/* Logo */}

          <div className="col-lg-4">

            <h2 className="footer-logo">
              WanderSync
            </h2>

            <p className="footer-text">
              WanderSync is an AI-powered Tours & Travels platform that
              helps travelers discover destinations, generate personalized
              itineraries and book unforgettable journeys across India.
            </p>

          </div>

          {/* Quick Links */}

          <div className="col-lg-2">

            <h5>Quick Links</h5>

            <ul className="footer-links">

              <li><a href="/">Home</a></li>

              <li><a href="/trips">Trips</a></li>

              <li><a href="/ai-planner">AI Planner</a></li>

              <li><a href="/contact">Contact</a></li>

            </ul>

          </div>

          {/* Contact */}

          <div className="col-lg-3">

            <h5>Contact</h5>

            <div className="footer-contact">

              <p>📍 Pune, Maharashtra</p>

              <p>📧 info@wandersync.com</p>

              <p>📞 +91 98765 43210</p>

            </div>

          </div>

          {/* Social */}

          <div className="col-lg-3">

            <h5>Follow Us</h5>

            <div className="social-icons">

              <a href="/">🌐</a>

              <a href="/">📘</a>

              <a href="/">📷</a>

              <a href="/">💼</a>

            </div>

            <p className="newsletter">
              Stay updated with our latest travel packages and AI features.
            </p>

          </div>

        </div>

        <hr />

        <div className="footer-bottom">

          © 2026 <strong>WanderSync</strong>. All Rights Reserved.

        </div>

      </div>

    </footer>
  );
}

export default Footer;