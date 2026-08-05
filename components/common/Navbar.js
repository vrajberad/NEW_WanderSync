import "./Navbar.css";

function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg fixed-top custom-navbar">

      <div className="container">

        {/* Logo */}
        <a className="navbar-brand logo" href="#hero">
          WanderSync
        </a>

        {/* Mobile Menu */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div
          className="collapse navbar-collapse"
          id="navbarContent"
        >

          <ul className="navbar-nav mx-auto">

            <li className="nav-item">
              <a className="nav-link" href="#hero">
                Home
              </a>
            </li>

            <li className="nav-item">
              <a className="nav-link" href="#search">
                Trips
              </a>
            </li>

            <li className="nav-item">
              <a className="nav-link" href="#ai-planner">
                AI Planner
              </a>
            </li>

            <li className="nav-item">
              <a className="nav-link" href="#about">
                About
              </a>
            </li>

            <li className="nav-item">
              <a className="nav-link" href="#contact">
                Contact
              </a>
            </li>

          </ul>

          <div className="d-flex gap-3">

            <a href="/login" className="btn login-btn">
              Login
            </a>

            <a href="/register" className="btn register-btn">
              Register
            </a>

          </div>

        </div>

      </div>

    </nav>
  );
}

export default Navbar;