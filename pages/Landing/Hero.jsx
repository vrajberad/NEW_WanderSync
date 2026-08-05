// import { Link } from "react-router-dom";
import "./Hero.css";
import heroImage from "../../assets/HeroSectionBackgroundImage.png";

function Hero() {
  return (
    <section
      id="hero"
      className="hero-section"
      style={{ backgroundImage: `url(${heroImage})` }}
    >
      <div className="hero-overlay">
        <div className="container h-100">
          <div className="row h-100 justify-content-center align-items-center">
            <div className="col-lg-9 text-center">

              <h1 className="hero-title">
                Plan Your Dream Journey
              </h1>

              <p className="hero-description">
                Explore beautiful destinations with AI-powered travel planning,
                smart recommendations, and hassle-free booking.
              </p>

              <div className="hero-buttons">

                <a
                  href="#search"
                  className="btn hero-btn-primary"
                >
                  Explore Trips
                </a>

                <a
                  href="#ai-planner"
                  className="btn hero-btn-secondary"
                >
                  AI Trip Planner
                </a>

              </div>

            </div>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Hero;