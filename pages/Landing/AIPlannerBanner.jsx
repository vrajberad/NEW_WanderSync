import { Link } from "react-router-dom";
import "./AIPlannerBanner.css";

import aiImage from "../../assets/ai-trip.webp";

function AIPlannerBanner() {
  return (
    <section id="ai-planner" className="ai-banner">

      <div className="container">

        <div className="row align-items-center h-100">

          {/* Left Image */}

          <div className="col-lg-7 d-flex justify-content-center">

            <div className="ai-image-box">

              <img
                src={aiImage}
                alt="AI Planner"
              />

            </div>

          </div>

          {/* Right Content */}

          <div className="col-lg-5 d-flex flex-column justify-content-center">

            <span className="ai-tag">
              ✨ AI Powered Travel Planner
            </span>

            <h2>
              Let AI Plan Your
              <br />
              Dream Vacation
            </h2>

            <p>
              Tell WanderSync your destination,
              budget, number of days and travel interests.
              Our AI instantly creates a personalized
              itinerary with hotels, sightseeing,
              transportation and budget estimation.
            </p>

            <div className="features">

              <div className="feature">
                <span>✅</span>
                <h5>Personalized Trip</h5>
              </div>

              <div className="feature">
                <span>💰</span>
                <h5>Budget Friendly</h5>
              </div>

              <div className="feature">
                <span>📅</span>
                <h5>Day Wise Plan</h5>
              </div>

              <div className="feature">
                <span>🏨</span>
                <h5>Hotels</h5>
              </div>

            </div>

            <Link
              to="/ai-planner"
              className="btn ai-btn"
            >
              Plan My Trip →
            </Link>

          </div>

        </div>

      </div>

    </section>
  );
}

export default AIPlannerBanner;