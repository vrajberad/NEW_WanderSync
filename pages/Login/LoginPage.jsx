import { Link } from "react-router-dom";
import "./LoginPage.css";
import backgroundImage from "../../assets/LoginRegister/backgroundImage.avif";

function LoginPage() {
  return (
    <div
      className="login-page"
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

        <div className="login-card">

          <div className="text-center mb-4">

            <h2>Welcome Back 👋</h2>

            <p>
              Login to continue your journey.
            </p>

          </div>

          <form>

            <div className="mb-3">

              <label>Email</label>

              <input
                type="email"
                className="form-control"
                placeholder="Enter your email"
              />

            </div>

            <div className="mb-3">

              <label>Password</label>

              <input
                type="password"
                className="form-control"
                placeholder="Enter your password"
              />

            </div>

            <div className="text-end mb-3">

              <Link to="/forgot-password">
                Forgot Password?
              </Link>

            </div>

            <button
              className="btn btn-primary w-100"
              type="submit"
            >
              Login
            </button>

          </form>

          <p className="mt-4 text-center">

            Don't have an account?

            <Link to="/register">
              {" "}Register
            </Link>

          </p>

        </div>

      </div>
    </div>
  );
}

export default LoginPage;