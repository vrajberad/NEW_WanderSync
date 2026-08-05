import { Link } from "react-router-dom";
import "./SearchSection.css";

function SearchSection() {
  return (
    <section id="search" className="search-section">

      <div className="container">

        <div className="search-card">

          <div className="search-header">

            <h1>Find Your Perfect Trip</h1>

            <p>
              Search destinations and discover amazing travel packages
              tailored to your budget and travel style.
            </p>

          </div>

          <div className="row align-items-center g-4">

            {/* Left */}

            <div className="col-lg-7">

              <div className="row g-3">

                <div className="col-12">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="📍 Destination"
                  />
                </div>

                <div className="col-md-6">
                  <input
                    type="date"
                    className="form-control"
                  />
                </div>

                <div className="col-md-6">
                  <input
                    type="number"
                    className="form-control"
                    placeholder="👥 Travelers"
                  />
                </div>

                <div className="col-md-6">
                  <select className="form-select">
                    <option>💰 Budget</option>
                    <option>₹10,000</option>
                    <option>₹20,000</option>
                    <option>₹50,000</option>
                    <option>₹1,00,000+</option>
                  </select>
                </div>

                <div className="col-md-6">
                  <select className="form-select">
                    <option>🏖 Trip Type</option>
                    <option>Beach</option>
                    <option>Mountain</option>
                    <option>Adventure</option>
                    <option>Wildlife</option>
                  </select>
                </div>

                <div className="col-12">

                  <button className="btn btn-primary search-btn">
                    🔍 Search Packages
                  </button>

                </div>

              </div>

            </div>

            {/* Right */}

            <div className="col-lg-5">

              <div className="ai-card">

                <span className="ai-badge">
                  🤖 AI Powered
                </span>

                <h2>
                  Don't Know Where To Go?
                </h2>

                <p>
                  Tell WanderSync your budget,
                  interests and duration.
                  Our AI will generate the perfect itinerary.
                </p>

                <Link
                  to="/ai-planner"
                  className="btn ai-btn"
                >
                  ✨ Plan With AI
                </Link>

              </div>

            </div>

          </div>

        </div>

      </div>

    </section>
  );
}

export default SearchSection;