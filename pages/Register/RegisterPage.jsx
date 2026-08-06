import { Link } from "react-router-dom";
import "./RegisterPage.css";
import backgroundImage from "../../assets/LoginRegister/backgroundImage.avif";

function RegisterPage() {
  return (
    <div
      className="register-page"
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <div className="auth-content">

        <div className="auth-brand">

          <div className="brand-icon">
            ✈
          </div>

          <h1>WanderSync</h1>

          <h5>Explore Without Limits</h5>

          <p>
            AI-powered travel planning • Smart itineraries • Secure booking • Personalized recommendations
          </p>

        </div>

        <div className="register-card">

          <div className="text-center mb-4">

            <h2>Create Your Account</h2><br/>

          </div>

          <form>

            <div className="row">

              <div className="col-md-6 mb-3">

                <label>First Name</label>

                <input
                  type="text"
                  className="form-control"
                  placeholder="First Name"
                />

              </div>

              <div className="col-md-6 mb-3">

                <label>Last Name</label>

                <input
                  type="text"
                  className="form-control"
                  placeholder="Last Name"
                />

              </div>

            </div>

            <div className="mb-3">

              <label>Email</label>

              <input
                type="email"
                className="form-control"
                placeholder="Enter Email"
              />

            </div>

            <div className="mb-3">

              <label>Phone Number</label>

              <input
                type="tel"
                className="form-control"
                placeholder="Enter Phone Number"
              />

            </div>

            <div className="row">

              <div className="col-md-6 mb-3">

                <label>Password</label>

                <input
                  type="password"
                  className="form-control"
                  placeholder="Enter Password"
                />

              </div>

              <div className="col-md-6 mb-3">

                <label>Confirm Password</label>

                <input
                  type="password"
                  className="form-control"
                  placeholder="Confirm Password"
                />

              </div>

            </div>

            <button
              className="btn btn-primary w-100"
              type="submit"
            >
              Register
            </button>

          </form>

          <p className="text-center mt-4">

            Already have an account?

            <Link to="/login">
              {" "}Login
            </Link>

          </p>

        </div>

      </div>
    </div>
  );
}

export default RegisterPage;